export class Table {
  constructor(
    public name: string,
    public buttons: number[],
    public width: number,
    public userId: number | undefined,
    public id?: number
  ) {}
}
export class Button {
  constructor(public points: number) {}
}
