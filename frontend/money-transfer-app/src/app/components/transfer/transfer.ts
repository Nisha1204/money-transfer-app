import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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
export class TransferComponent implements OnInit {
  transferForm: FormGroup;
  accounts: any[] = [];
  loading = false;
  message = '';
  isError = false;
  toAccountValid = false; // New state to track if destination exists

  constructor(private fb: FormBuilder, private transferService: TransferService, private cdr: ChangeDetectorRef) {
    this.transferForm = this.fb.group({
      fromAccountId: ['', Validators.required], // Removed disabled:true to allow programmatic reset
      toAccountId: ['', Validators.required],
      amount: [{ value: '', disabled: true }, [Validators.required, Validators.min(0.01)]]
    });
  }

  // ... keep ngOnInit and loadUserAccounts as they are ...
  ngOnInit() {
    setTimeout(() => {
    this.loadUserAccounts(1); 
  });
  }

loadUserAccounts(accountId: number) {
  this.loading = true;
  this.transferService.getAccountData(accountId).subscribe({
    next: (data) => {
      if (data && data.owner && data.owner.accounts) {
        this.accounts = data.owner.accounts;
        
        // Find the account we just used
        const selected = this.accounts.find(acc => acc.id === Number(accountId));
        if (selected) {
          // IMPORTANT: Update the form so the dropdown and readonly input sync
          this.transferForm.patchValue({ fromAccountId: selected.id }, { emitEvent: false });
          this.message = `Updated Balance: $${selected.balance}`;
        }
      }
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      this.loading = false;
      this.isError = true;
      this.message = err.error?.message || 'Error loading accounts.';
      this.cdr.detectChanges();
    }
  });
}

onAccountSelect(accountId: any) {
  const id = Number(accountId);
  const selected = this.accounts.find(a => a.id === id);
  
  if (selected) {
    this.transferForm.patchValue({ fromAccountId: selected.id });
    this.message = `Selected Account Balance: $${selected.balance}`;
    this.isError = false;
    this.validateToAccount();
  }
  this.cdr.detectChanges();
}

  // NEW: Validate "To Account" on blur
  validateToAccount() {
  const toId = this.transferForm.get('toAccountId')?.value;
  const fromId = this.transferForm.get('fromAccountId')?.value;

  if (!toId) {
    this.toAccountValid = false;
    this.transferForm.get('amount')?.disable();
    return;
  }

  if (toId === fromId) {
    this.toAccountValid = false;
    this.isError = true;
    this.message = "Source and destination accounts must be different.";
    this.transferForm.get('amount')?.disable();
    this.transferForm.get('amount')?.setValue('');
  } else {
    // If they aren't the same, we assume it's valid for now 
    // and let the final transfer call handle the existence check.
    this.toAccountValid = true;
    this.isError = false;
    this.message = ""; 
    this.transferForm.get('amount')?.enable();
  }
  this.cdr.detectChanges();
}

/*
// Helper to avoid code duplication
private handleValidRecipient(id: any) {
  this.toAccountValid = true;
  this.isError = false;
  this.message = `Recipient Verified: ${id}`;
  this.transferForm.get('amount')?.enable();
  this.cdr.detectChanges();
}
  */

 onTransfer() {
  // 1. Basic UI Guard
  if (!this.toAccountValid) {
    this.message = "Please enter a valid recipient account first.";
    this.isError = true;
    return;
  }

  if (this.transferForm.invalid) return;

  this.loading = true;
  this.message = '';
  this.isError = false;

  const { fromAccountId, toAccountId, amount } = this.transferForm.getRawValue();

  this.transferService.executeTransfer(fromAccountId, toAccountId, amount)
    .subscribe({
      next: (res) => {
        this.loading = false;
        this.isError = false; // Reset error state on success
        this.message = `Success! ${res.message || 'Transfer completed.'}`;

        const currentFrom = fromAccountId; 
        this.transferForm.reset(); 
        this.toAccountValid = false;
        this.transferForm.get('amount')?.disable();
        this.loadUserAccounts(currentFrom); 
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        this.isError = true; // Set error state to true

        // Extracting the message: 
        // 1. err.error.message (Standard Spring Boot)
        // 2. err.error (Plain string)
        // 3. err.message (HTTP failure)
        this.message = err.error?.message || err.error || err.message || 'An unexpected error occurred.';
        
        console.log("Setting UI message to:", this.message); // Debug check
        this.cdr.detectChanges();
      }
    });
}

  clearForm() {
  this.transferForm.reset();
  this.toAccountValid = false;
  this.transferForm.get('amount')?.disable();
  this.message = 'Form cleared.';
  this.isError = false;
  this.cdr.detectChanges();
}

}
