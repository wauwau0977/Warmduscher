import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoilerChartComponent } from './boiler-chart.component';

describe('BoilerChartComponent', () => {
  let component: BoilerChartComponent;
  let fixture: ComponentFixture<BoilerChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoilerChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoilerChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
