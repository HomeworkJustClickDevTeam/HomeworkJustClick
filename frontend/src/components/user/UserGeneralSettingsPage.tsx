import React, { useEffect, useState } from "react"
import { changeUserIndexPostgresService} from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"

export default function UserGeneralSettingsPage() {
  const userState = useAppSelector(selectUserState)
  const [index, setIndex] = useState<number | undefined>(userState?.index)

  const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    if (userState !== null) {
      event.preventDefault()
      await changeUserIndexPostgresService(userState.id.toString(), index as number)
        .catch((error: AxiosError) => {
          console.log(error)
        })
    }
  }


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
        <input type="submit" value="Potwierdź"/>
      </form>
    </>
  )
}
