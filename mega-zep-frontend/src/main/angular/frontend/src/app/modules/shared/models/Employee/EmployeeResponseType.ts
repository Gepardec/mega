import {ResponseHeaderType} from "../ResponseHeaderType";
import {Employee} from "./Employee";

export class EmployeeResponseType {
  private _responseHeader: ResponseHeaderType;
  private _length: number;
  private _employeeList: Array<Employee>;

  get responseHeader(): ResponseHeaderType {
    return this._responseHeader;
  }

  set responseHeader(value: ResponseHeaderType) {
    this._responseHeader = value;
  }

  get length(): number {
    return this._length;
  }

  set length(value: number) {
    this._length = value;
  }

  get employeeList(): Array<Employee> {
    return this._employeeList;
  }

  set employeeList(value: Array<Employee>) {
    this._employeeList = value;
  }
}
