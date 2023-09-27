import { GroupInterface } from "../types/GroupInterface"

interface GroupFilterProps {
  setGroups: (groups: GroupInterface[] | undefined) => void
  setIsLoading: (isLoading:boolean) => void
  userId: string
}

export const groupFilter = ({setGroups, setIsLoading, userId}: GroupFilterProps) => {

  const studentsUserGroups = (): void => {

  }
  const allUserGroups = (): void => {
    setIsLoading(true)

    setIsLoading(false)
  }
  return {
    studentsUserGroups,
    allUserGroups,
  }
}
