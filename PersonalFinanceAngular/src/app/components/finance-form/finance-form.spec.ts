import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanceForm } from './finance-form';

describe('FinanceForm', () => {
  let component: FinanceForm;
  let fixture: ComponentFixture<FinanceForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinanceForm],
    }).compileComponents();

    fixture = TestBed.createComponent(FinanceForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
