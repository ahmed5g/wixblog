import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DividerBlock } from './divider-block';

describe('DividerBlock', () => {
  let component: DividerBlock;
  let fixture: ComponentFixture<DividerBlock>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DividerBlock]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DividerBlock);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
