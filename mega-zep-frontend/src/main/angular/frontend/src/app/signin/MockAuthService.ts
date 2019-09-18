import {Observable} from "rxjs";
import {SocialUser} from "angularx-social-login";

export class MockAuthService {

  readonly authState: Observable<SocialUser>;

  constructor() {
    this.authState = new Observable<SocialUser>();
  }


  signinWithGoogle() {
    const user: SocialUser = new SocialUser();
    user.id = "123456879";
    user.email = "christoph.ruhsam@gepardec.com";
    user.authToken = "987654321";
    const promise: Promise<SocialUser> = new Promise<SocialUser>((resolve, reject) => {
      resolve(user);
    });
    return promise;
  }

  signOut() {
    const promise: Promise<SocialUser> = new Promise<SocialUser>((resolve, reject) => {
      resolve(null);
    });
    return promise;
  }
}
