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
  max_points: number
  auto_penalty: number
}