import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormGroupDirective } from '@angular/forms';
import { TransferService } from '../../services/transfer-service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar } from '@angular/material/snack-bar'; // Import SnackBar
import { finalize } from 'rxjs'; // 1. Add this import

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatIconModule],
  templateUrl: './transfer.html',
  styleUrls: ['./transfer.css']
})

export class TransferComponent implements OnInit {
  @ViewChild(FormGroupDirective) formGroupDirective!: FormGroupDirective;

  transferForm: FormGroup;
  accounts: any[] = [];
  loading = false;
  toAccountValid = false;

  constructor(
    private fb: FormBuilder, 
    private transferService: TransferService, 
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar // Inject SnackBar
  ) {
    this.transferForm = this.fb.group({
      fromAccountId: ['', Validators.required], 
      toAccountId: [{ value: '', disabled: true }, [Validators.required, Validators.pattern('^[0-9]*$')]],
      amount: [{ value: '', disabled: true }, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit() {
    this.loadUserAccounts(1);
  }

  loadUserAccounts(accountId: number) {
    this.transferService.getAccountData(accountId).subscribe({
      next: (data) => {
        if (data?.owner?.accounts) this.accounts = data.owner.accounts;
      }
    });
  }

  // FIX 1: Explicitly patch the value so the readonly input sees it
  onAccountSelect(accountId: any) {
    const id = Number(accountId);
    this.transferForm.patchValue({ fromAccountId: id }); // This ensures the readonly input updates
    
    this.transferForm.get('toAccountId')?.enable();
    this.transferForm.get('toAccountId')?.setValue('');
    this.transferForm.get('amount')?.disable();
    this.transferForm.get('amount')?.setValue('');
    
    this.toAccountValid = false;
    this.cdr.detectChanges();
  }

  validateToAccount() {
    const toControl = this.transferForm.get('toAccountId');
    const fromId = this.transferForm.get('fromAccountId')?.value;
    const toId = toControl?.value;

    this.toAccountValid = false;
    this.transferForm.get('amount')?.disable();

    if (!toId) return;

    if (toControl?.errors?.['pattern']) return;

    if (Number(toId) === Number(fromId)) {
      toControl?.setErrors({ sameAccount: true });
    } else {
      this.toAccountValid = true;
      this.transferForm.get('amount')?.enable();
    }
  }

onTransfer() {
  if (this.transferForm.invalid) return;
  
  this.loading = true;
  const { fromAccountId, toAccountId, amount } = this.transferForm.getRawValue();

  this.transferService.executeTransfer(fromAccountId, toAccountId, amount)
    .pipe(
      finalize(() => {
        this.loading = false;
        this.cdr.detectChanges();
      })
    )
    .subscribe({
      next: (res) => {
        this.showSnackbar(`Success! Transfer completed.`, 'success');
        // Save the current sender ID to reload their balance after reset
        const lastFrom = fromAccountId;
        this.clearForm();
        this.loadUserAccounts(lastFrom);
      },
      error: (err) => {
        let errorMsg = 'An unexpected error occurred.';
        
        // Handle specific 404/Not Found logic
        if (err.status === 404 || err.error?.message?.includes('not found')) {
          errorMsg = 'Recipient account ID not found.';
          
          // --- THE FIX: Clear the form on error ---
          //this.clearForm(); 
        } else {
          errorMsg = err.error?.message || err.error || 'Transfer failed.';
        }

        this.showSnackbar(errorMsg, 'error');
      }
    });
}
  showSnackbar(message: string, type: 'success' | 'error') {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: type === 'success' ? ['green-snackbar'] : ['red-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  clearForm() {
    // 1. Reset UI validation states (removes red borders)
    if (this.formGroupDirective) {
      this.formGroupDirective.resetForm();
    }

    // 2. Reset data values
    this.transferForm.reset();

    // 3. Re-enforce the disabled hierarchy
    // Only fromAccountId select should be enabled initially
    this.transferForm.get('toAccountId')?.disable();
    this.transferForm.get('amount')?.disable();
    
    this.toAccountValid = false;
    this.cdr.detectChanges();
  }
}