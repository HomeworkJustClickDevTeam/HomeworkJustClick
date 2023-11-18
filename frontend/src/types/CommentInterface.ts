import { CommentCreateInterface } from "./CommentCreateInterface"

export interface CommentInterface extends CommentCreateInterface{
  lastUsedDate: Date
  counter: number
  id:number
}