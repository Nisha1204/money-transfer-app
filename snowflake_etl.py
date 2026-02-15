import pymysql
import snowflake.connector
from snowflake.connector.pandas_tools import write_pandas
import pandas as pd
from sqlalchemy import create_engine
import datetime

# --- CONFIGURATION ---
MYSQL_URI = "mysql+pymysql://root:Root123$@127.0.0.1/money_db"

SNOWFLAKE_CONFIG = {
    'user': 'Nisha',
    'password': 'Snowflake@Nisha123',
    'account': 'BEAFINV-FHC86386',
    'warehouse': 'COMPUTE_WH',
    'database': 'MONEY_DB',
    'schema': 'ANALYTICS',
    'role': 'ACCOUNTADMIN'
}

def get_col(df, options):
    for opt in options:
        if opt in df.columns: 
            return opt
    return None

def run_etl():
    print("--- 1. Extracting from MySQL ---")
    try:
        engine = create_engine(MYSQL_URI)
        df_users_raw = pd.read_sql("SELECT * FROM users", engine)
        df_accounts_raw = pd.read_sql("SELECT * FROM accounts", engine)
        df_trans_raw = pd.read_sql("SELECT * FROM transaction_logs", engine)
        print(f"📊 Data Found: {len(df_users_raw)} Users, {len(df_accounts_raw)} Accounts, {len(df_trans_raw)} Logs")
    except Exception as e:
        print(f"❌ MySQL Error: {e}")
        return

    print("--- 2. Transforming for Snowflake ---")
    try:
        # DIM_USERS transformation
        df_users = df_users_raw[['id', 'username', 'role']].copy()
        df_users.columns = ['USER_ID', 'USERNAME', 'ROLE']
        
        # DIM_ACCOUNTS transformation
        h_name = get_col(df_accounts_raw, ['holder_name', 'holderName'])
        u_id = get_col(df_accounts_raw, ['user_id', 'userId'])
        df_accounts = df_accounts_raw[['id', h_name, 'balance', 'status', u_id]].copy()
        df_accounts.columns = ['ACCOUNT_ID', 'HOLDER_NAME', 'BALANCE', 'STATUS', 'USER_ID']
        
        # FACT_TRANSACTIONS transformation
        f_acc = get_col(df_trans_raw, ['from_account_id', 'fromAccountId'])
        t_acc = get_col(df_trans_raw, ['to_account_id', 'toAccountId'])
        c_on = get_col(df_trans_raw, ['created_on', 'createdOn'])

        df_trans = df_trans_raw[['id', f_acc, t_acc, 'amount', 'status', c_on]].copy()

        # Rename columns FIRST
        df_trans = df_trans.rename(columns={
            'id': 'TRANSACTION_ID',
            f_acc: 'FROM_ACCOUNT_ID',
            t_acc: 'TO_ACCOUNT_ID',
            'amount': 'AMOUNT',
            'status': 'STATUS',
            c_on: 'CREATED_ON'
        })

        # Convert to datetime and ensure it's timezone-naive
        df_trans['CREATED_ON'] = pd.to_datetime(df_trans['CREATED_ON'])
        if df_trans['CREATED_ON'].dt.tz is not None:
            df_trans['CREATED_ON'] = df_trans['CREATED_ON'].dt.tz_localize(None)
        
        # Add DATE_KEY for analytics (as DATE type, not datetime)
        df_trans['DATE_KEY'] = df_trans['CREATED_ON'].dt.date
        
        # CRITICAL FIX: Convert datetime64[ns] to object dtype with proper timestamp format
        # This ensures write_pandas recognizes it as a timestamp, not a number
        df_trans['CREATED_ON'] = df_trans['CREATED_ON'].dt.strftime('%Y-%m-%d %H:%M:%S')
        
        # Convert back to datetime so Snowflake recognizes it properly
        df_trans['CREATED_ON'] = pd.to_datetime(df_trans['CREATED_ON'])

        # Ensure all columns are uppercase
        df_users.columns = [x.upper() for x in df_users.columns]
        df_accounts.columns = [x.upper() for x in df_accounts.columns]
        df_trans.columns = [x.upper() for x in df_trans.columns]
        
        print(f"✅ Transformation complete")
        print(f"   CREATED_ON dtype: {df_trans['CREATED_ON'].dtype}")
        print(f"   Sample values: {df_trans['CREATED_ON'].head().tolist()}")
            
    except Exception as e:
        print(f"❌ Transformation Error: {e}")
        import traceback
        traceback.print_exc()
        return

    print("--- 3. Loading to Snowflake ---")
    try:
        ctx = snowflake.connector.connect(**SNOWFLAKE_CONFIG)
        
        print("Uploading DIM_USERS...")
        write_pandas(ctx, df_users, 'DIM_USERS', overwrite=True)
        
        print("Uploading DIM_ACCOUNTS...")
        write_pandas(ctx, df_accounts, 'DIM_ACCOUNTS', overwrite=True)
        
        print("Uploading FACT_TRANSACTIONS...")
        # Use auto_create_table=True to let Snowflake infer types
        write_pandas(ctx, df_trans, 'FACT_TRANSACTIONS', overwrite=True, auto_create_table=True)
        
        print("🚀 SUCCESS: Snowflake Data Warehouse updated!")
        ctx.close()

    except Exception as e:
        print(f"❌ Snowflake Error: {e}")
        import traceback
        traceback.print_exc()
        
if __name__ == "__main__":
    print("ETL Script Started...")
    run_etl()
    print("ETL Script Finished.")