export const CalculateIfHandedLate = (dueDate: Date) =>{
  return new Date().getTime() > new Date(dueDate).getTime();
}