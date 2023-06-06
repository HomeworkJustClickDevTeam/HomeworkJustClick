import { useState } from "react"
import {
  Assigment,
  Solution,
  SolutionTypesProp,
  UserToShow,
  UserWithAssignment,
} from "../../../types/types"
import postgresqlDatabase from "../../../services/default-request-database"
import { AxiosResponse } from "axios"

export const SolutionFilter = ({
  id,
  setUsersWithAssignment,
}: SolutionTypesProp) => {
  const [solutions, setSolutions] = useState<Solution[]>([])
  const fetchUserAndAssignment = (
    solution: Solution
  ): Promise<[AxiosResponse<UserToShow>, AxiosResponse<Assigment>]> => {
    return Promise.all([
      postgresqlDatabase.get<UserToShow>(`/user/${solution.userId}`),
      postgresqlDatabase.get<Assigment>(`/assignment/${solution.assignmentId}`),
    ])
  }

  const handlePromiseResults = (
    fetchedSolutions: Solution[],
    results: [AxiosResponse<UserToShow>, AxiosResponse<Assigment>][]
  ): UserWithAssignment[] => {
    return results.map(([userResult, assignmentResult], index) => ({
      user: userResult.data,
      assignment: assignmentResult.data,
      solution: fetchedSolutions[index],
    }))
  }
  const lateSolutions = (): Promise<void> => {
    return postgresqlDatabase
      .get(`/solutions/lateByGroup/${id}`)
      .then((r) => {
        const fetchedSolutions: Solution[] = r.data
        setSolutions(fetchedSolutions)

        const promises: Promise<
          [AxiosResponse<UserToShow>, AxiosResponse<Assigment>]
        >[] = fetchedSolutions.map((solution) =>
          Promise.all([
            postgresqlDatabase.get<UserToShow>(`/user/${solution.userId}`),
            postgresqlDatabase.get<Assigment>(
              `/assignment/${solution.assignmentId}`
            ),
          ])
        )

        Promise.all(promises)
          .then((results) => {
            const usersWithAssignment: UserWithAssignment[] = results.map(
              ([userResult, assignmentResult], index) => ({
                user: userResult.data,
                assignment: assignmentResult.data,
                solution: fetchedSolutions[index],
              })
            )

            setUsersWithAssignment(usersWithAssignment)
          })
          .catch()
      })
      .catch(() => {
        setUsersWithAssignment([])
      })
  }
  const uncheckedSolutions = (): Promise<void> => {
    return postgresqlDatabase
      .get(`/solutions/uncheckedByGroup/${id}`)
      .then((response) => {
        const fetchedSolutions: Solution[] = response.data
        setSolutions(fetchedSolutions)

        const promises: Promise<
          [AxiosResponse<UserToShow>, AxiosResponse<Assigment>]
        >[] = fetchedSolutions.map((solution) =>
          fetchUserAndAssignment(solution)
        )

        Promise.all(promises)
          .then((results) => {
            const usersWithAssignment: UserWithAssignment[] =
              handlePromiseResults(fetchedSolutions, results)
            setUsersWithAssignment(usersWithAssignment)
          })
          .catch()
      })
      .catch(() => {
        setUsersWithAssignment([])
      })
  }

  const checkSolutions = (): Promise<void> => {
    return postgresqlDatabase
      .get(`/solutions/checkedByGroup/${id}`)
      .then((response) => {
        const fetchedSolutions: Solution[] = response.data
        setSolutions(fetchedSolutions)

        const promises: Promise<
          [AxiosResponse<UserToShow>, AxiosResponse<Assigment>]
        >[] = fetchedSolutions.map((solution) =>
          fetchUserAndAssignment(solution)
        )

        Promise.all(promises)
          .then((results) => {
            const usersWithAssignment: UserWithAssignment[] =
              handlePromiseResults(fetchedSolutions, results)
            setUsersWithAssignment(usersWithAssignment)
          })
          .catch()
      })
      .catch(() => {
        setUsersWithAssignment([])
      })
  }
  return {
    checkSolutions,
    uncheckedSolutions,
    lateSolutions,
  }
}
