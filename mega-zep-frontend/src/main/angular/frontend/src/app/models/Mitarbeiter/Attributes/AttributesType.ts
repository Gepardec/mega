import {AttributeType} from "./AttributeType";

export class AttributesType {
  private _attribute: Array<AttributeType>;

  get attribute(): Array<AttributeType> {
    return this._attribute;
  }

  set attribute(value: Array<AttributeType>) {
    this._attribute = value;
  }
}
