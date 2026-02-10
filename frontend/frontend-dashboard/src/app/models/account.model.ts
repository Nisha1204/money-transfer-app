export interface User {
  id: number;
  username: string;
  role: string;
  accounts: Account[];
}

export interface Account {
  id: number;
  holderName: string;
  balance: number;
  status: AccountStatus;
  version: number;
  updatedAt: string;
  active: boolean;
}

export interface AccountResponse {
  id: number;
  holderName: string;
  balance: number;
  status: AccountStatus;
  updatedAt: string;
  owner: User;
}

export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  LOCKED = 'LOCKED',
  CLOSED = 'CLOSED'
}