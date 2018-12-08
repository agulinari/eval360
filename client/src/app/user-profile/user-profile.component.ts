import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../shared/user.service';
import { Subscription } from 'rxjs';
import { User } from '../domain/user/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {

  user: User;
  sub: Subscription;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) { }

    ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
        const id = params['id'];
        if (id) {
          this.getUser(id);
        }
      });
    }

  getUser(id) {
    this.userService.get(id).subscribe((user: User) => {
      if (user) {
        this.user = user;
      } else {
        console.log(`Usuario con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    });
  }

  gotoList() {
    this.router.navigate(['/main/user-list']);
  }

  editUser() {
    this.router.navigate([`/main/user-edit/${this.user.id}`]);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }


}
