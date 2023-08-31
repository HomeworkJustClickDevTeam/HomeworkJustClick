import React, {useContext, useEffect, useState} from "react"
import {getUserPostgresService, changeUserColorPostgresService} from "../../services/postgresDatabaseServices"
import {AxiosError} from "axios"
import {useNavigate} from "react-router-dom";
import {getUser} from "../../services/otherServices";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function UserAppearanceSettingsPage() {
  const [color, setColor] = useState<number | undefined>(undefined)
  const {applicationState} = useContext(ApplicationStateContext)

  const navigate = useNavigate()


  const handleColorChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setColor(+event.target.value)
    try {
      if (applicationState?.userState !== undefined) {
        await changeUserColorPostgresService(applicationState?.userState.id.toString(), +event.target.value)
          .catch((error: AxiosError) => {
            console.log("AXIOS ERROR: ", error)
          })
      }
    } catch (e) {
      console.log(e)
    }
  }
  useEffect(() => {
    if (applicationState?.userState !== undefined) {
      getUserPostgresService(applicationState?.userState.id.toString())
        .then((response) => setColor(response.data.color))
        .catch(() => setColor(undefined))
    }
  }, [applicationState?.userState?.id])
  return (
    <>
      <p>Wybierz swój kolor:</p>
      <form>
        {Array.apply(0, Array(20)).map((x, i) => {
          return (
            <label key={i}>
              <input
                type="radio"
                value={i.toString()}
                name="index"
                checked={color === i}
                onChange={handleColorChange}
              />
              {i}
            </label>
          )
        })}
      </form>
    </>
  )
}
