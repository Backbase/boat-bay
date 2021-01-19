import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from "../../core/login/login.service";
import { AccountService } from "../../core/auth/account.service";
import { LoginModalService } from "../../core/login/login-modal.service";
import { IMAGES_DEFAULT_ASSET_PATH } from "../../app.constants";


@Component({
  selector: 'bb-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss'],
})
export class NavbarComponent implements OnInit {
  isNavbarCollapsed = true;
  imageAssetsPath: string;
  public isScrolling = false;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private router: Router,
    @Inject(IMAGES_DEFAULT_ASSET_PATH) private imageAssetsPathSetting: string
  ) {
    this.imageAssetsPath = imageAssetsPathSetting;
  }

  ngOnInit(): void {

  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  onWindowScroll(event: any): void {
    this.isScrolling = event.target.defaultView.scrollY > 50;
  }
}
