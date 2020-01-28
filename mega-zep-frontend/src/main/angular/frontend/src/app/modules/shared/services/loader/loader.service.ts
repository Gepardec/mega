import { Injectable } from '@angular/core';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { MatSpinner } from '@angular/material/progress-spinner';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private spinnerRef: OverlayRef = this.cdkSpinnerCreate();
  private requests = 0;

  constructor(private overlay: Overlay) {
  }

  private cdkSpinnerCreate() {
    return this.overlay.create({
      hasBackdrop: true,
      backdropClass: 'dark-backdrop',
      positionStrategy: this.overlay.position()
        .global()
        .centerHorizontally()
        .centerVertically()
    });
  }

  showSpinner() {
    this.requests++;
    if (!this.spinnerRef.hasAttached()) {
      this.spinnerRef.attach(new ComponentPortal(MatSpinner));
    }
  }

  stopSpinner() {
    this.requests--;
    if (this.requests === 0) {
      this.spinnerRef.detach();
    }
  }
}
