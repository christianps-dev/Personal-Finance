import { TestBed } from '@angular/core/testing';

import { TabelaServices } from './chart-services';

describe('ChartServices', () => {
  let service: TabelaServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TabelaServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
