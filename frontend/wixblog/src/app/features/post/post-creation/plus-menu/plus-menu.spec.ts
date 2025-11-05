import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlusMenu } from './plus-menu';

describe('PlusMenu', () => {
  let component: PlusMenu;
  let fixture: ComponentFixture<PlusMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlusMenu]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlusMenu);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
