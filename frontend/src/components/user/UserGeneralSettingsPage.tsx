

import React, {useContext, useEffect, useState} from "react"
import {getUserPostgresService, putUserIndexPostgresService} from "../../services/postgresDatabaseServices"
import {AxiosError} from "axios"
import {getUser} from "../../services/otherServices";
import Loading from "../animations/Loading";
import HomePage from "../home/HomePage";
import {useNavigate} from "react-router-dom";

export default function UserGeneralSettingsPage() {
  const userState = getUser()
  const [index, setIndex] = useState<number | undefined>(undefined)
  const navigate = useNavigate()

  useEffect(() => {
    if(userState !== undefined) {
      getUserPostgresService(userState.id.toString())
        .then((response) => setIndex(response.data.index))
        .catch(() => setIndex(undefined))
    }
  }, [userState?.id])


  if(userState === undefined){
    navigate("/")
  }
  const handleIndexSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    if(userState !== undefined) {
      event.preventDefault()
      await putUserIndexPostgresService(userState.id.toString(), index as number)
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
        <input type="submit" value="Potwierdź" />
      </form>
    </>
  )
}
