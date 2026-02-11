import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TransferService } from '../../services/transfer-service';
import { finalize } from 'rxjs'; // Add this import

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transfer.html',
  styleUrls: ['./transfer.css']
})
export class TransferComponent {
  transferForm: FormGroup;
  loading = false;
  message = '';
  isError = false;

  constructor(private fb: FormBuilder, private transferService: TransferService) {
    this.transferForm = this.fb.group({
      fromAccountId: ['', Validators.required],
      toAccountId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  onTransfer() {
  if (this.transferForm.invalid) return;

  this.loading = true;
  this.message = '';
  const { fromAccountId, toAccountId, amount } = this.transferForm.value;

  this.transferService.executeTransfer(fromAccountId, toAccountId, amount)
    .pipe(
      finalize(() => this.loading = false) // This ensures loading stops NO MATTER WHAT
    )
    .subscribe({
      next: (res) => {
        this.isError = false;
        this.message = `Success! Transaction ID: ${res.id}`;
        this.transferForm.reset();
      },
      error: (err) => {
        this.isError = true;
        console.error('Full error details:', err); // Check console (F12) for this!
        this.message = err.error?.message || 'Connection failed or Unauthorized.';
      }
    });
  }

}