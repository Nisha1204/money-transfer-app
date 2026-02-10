import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BalanceView } from './balance-view';

describe('BalanceView', () => {
  let component: BalanceView;
  let fixture: ComponentFixture<BalanceView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BalanceView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BalanceView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
