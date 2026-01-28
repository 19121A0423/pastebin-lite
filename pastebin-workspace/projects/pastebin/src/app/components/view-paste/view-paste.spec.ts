import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPaste } from './view-paste';

describe('ViewPaste', () => {
  let component: ViewPaste;
  let fixture: ComponentFixture<ViewPaste>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewPaste]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewPaste);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
