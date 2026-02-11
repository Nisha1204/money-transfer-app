export interface TransferRequest {
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  idempotencyKey: string;
}

export interface TransferResponse {
  id: string;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  status: 'SUCCESS' | 'FAILED';
  message: string;
}