export class ProjectComment {
  id: number
  comment: string
  date: string
  projectName: string


  constructor(comment: string, yearMonth: string, projectName: string) {
    this.comment = comment;
    this.date = yearMonth;
    this.projectName = projectName;
  }
}
