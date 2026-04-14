import { TestBed } from '@angular/core/testing';

import { Finances } from './finances';

describe('Finances', () => {
  let service: Finances;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Finances);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
