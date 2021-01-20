import { Injectable } from '@angular/core';
import { LoginModalComponent } from "../../login/login.component";
import { MatDialog } from "@angular/material/dialog";


@Injectable({ providedIn: 'root' })
export class LoginModalService {
  private isOpen = false;

  constructor(public dialog: MatDialog) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;

    const dialogRef = this.dialog.open(LoginModalComponent);
    dialogRef.afterClosed().subscribe(result => this.isOpen = false);
  }
}
