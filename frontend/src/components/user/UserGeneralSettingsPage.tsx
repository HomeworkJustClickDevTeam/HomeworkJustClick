import React, {useContext, useEffect, useState} from "react"
import {getUserPostgresService, putUserIndexPostgresService} from "../../services/postgresDatabaseServices"
import {AxiosError} from "axios"
import {getUser} from "../../services/otherServices";
import {useNavigate} from "react-router-dom";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function UserGeneralSettingsPage() {
  const {applicationState} = useContext(ApplicationStateContext)
  const [index, setIndex] = useState<number | undefined>(undefined)
  const navigate = useNavigate()

  useEffect(() => {
    if (applicationState?.userState !== undefined) {
      getUserPostgresService(applicationState?.userState.id.toString())
        .then((response) => setIndex(response.data.index))
        .catch(() => setIndex(undefined))
    }
  }, [applicationState?.userState?.id])


  if (applicationState?.userState === undefined) {
    navigate("/")
  }
  const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    if (applicationState?.userState !== undefined) {
      event.preventDefault()
      await putUserIndexPostgresService(applicationState?.userState.id.toString(), index as number)
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
