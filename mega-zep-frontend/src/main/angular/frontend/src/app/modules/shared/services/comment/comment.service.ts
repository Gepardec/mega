import {Injectable} from '@angular/core';
import {Comment} from '../../models/Comment';
import {State} from '../../models/State';
import {MonthlyReport} from "../../../monthly-report/models/MonthlyReport";
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "../config/config.service";
import {Observable} from "rxjs";
import {Employee} from "../../models/Employee";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private httpClient: HttpClient,
    private config: ConfigService
  ) {
  }

  getDoneCommentsCount(comments: Array<Comment>): number {
    return this.getDoneComments(comments).length;
  }

  getDoneComments(comments: Array<Comment>): Array<Comment> {
    return comments.filter(comment => comment.state === State.DONE);
  }

  areAllCommentsDone(comments: Array<Comment>): boolean {
    return this.getDoneCommentsCount(comments) === comments.length;
  }

  setStatusDone(comment: Comment): Observable<number> {
    return this.httpClient.put<number>(this.config.getBackendUrlWithContext('/comments/setdone'), comment);
  }

  // getCommentsForEmployee(employee: Employee){
  //   return this.httpClient.get<Array<Comment>>(this.config.getBackendUrlWithContext('/comments/getall'), employee);
  // }
}
