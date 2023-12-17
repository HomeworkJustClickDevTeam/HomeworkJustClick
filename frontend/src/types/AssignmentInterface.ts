export interface AssignmentInterface {
  id: number
  title: string
  visible: boolean
  userId: number
  groupId: number
  taskDescription: string
  creationDatetime: Date
  lastModifiedDatetime: Date
  completionDatetime: Date
  maxPoints: number
  autoPenalty: number
}