import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePaste } from './create-paste';

describe('CreatePaste', () => {
  let component: CreatePaste;
  let fixture: ComponentFixture<CreatePaste>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreatePaste]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatePaste);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
