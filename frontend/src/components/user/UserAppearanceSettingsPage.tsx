import React, {useContext, useEffect, useState} from "react"
import {getUserPostgresService, putUserColorPostgresService} from "../../services/postgresDatabase"
import userContext from "../../contexts/UserContext"
import {AxiosError} from "axios"

export default function UserAppearanceSettingsPage() {
  const [color, setColor] = useState<number | undefined>(undefined)
  const { loggedIn, userState } = useContext(userContext)

  const handleColorChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setColor(+event.target.value)
    try {
      await putUserColorPostgresService(userState.userId, +event.target.value)
        .catch((error: AxiosError) => {
          console.log("AXIOS ERROR: ", error)
        })
    } catch (e) {
      console.log(e)
    }
  }
  useEffect(() => {
    getUserPostgresService(userState.userId)
      .then((response) => setColor(response.data.color))
      .catch(() => setColor(undefined))
  }, [userState.userId])
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
