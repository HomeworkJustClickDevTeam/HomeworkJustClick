import userContext from "../../contexts/UserContext"

import React, { useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import { AxiosError } from "axios"

export default function UserGeneralSettingsPage() {
  const { loggedIn, userState } = useContext(userContext)
  const [index, setIndex] = useState<number | undefined>(undefined)

  const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    await postgresqlDatabase
      .put(`/user/index/${userState.userId}`, index)
      .catch((error: AxiosError) => {
        console.log(error)
      })
  }

  useEffect(() => {
    postgresqlDatabase
      .get(`/user/${userState.userId}`)
      .then((response) => setIndex(response.data.index))
      .catch(() => setIndex(undefined))
  }, [userState.userId])
  return (
    <>
      <form onSubmit={handleIndexSubmit}>
        <label htmlFor="index">Indeks: </label>
        <input
          type="number"
          id="index"
          name="index"
          defaultValue={index}
          onChange={(e) => setIndex(+e.target.value)}
        />
        <input type="submit" value="Potwierdź" />
      </form>
    </>
  )
}
