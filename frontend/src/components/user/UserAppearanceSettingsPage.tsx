import React, { useContext, useEffect, useState } from "react"
import { changeUserColorPostgresService, getUserPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { useNavigate } from "react-router-dom"
import { useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"

export default function UserAppearanceSettingsPage() {
  const [color, setColor] = useState<number | undefined>(undefined)
  const userState = useAppSelector(selectUserState)

  const handleColorChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setColor(+event.target.value)
    try {
      if (userState !== null) {
        await changeUserColorPostgresService(userState.id.toString(), +event.target.value)
          .catch((error: AxiosError) => {
            console.log("AXIOS ERROR: ", error)
          })
      }
    } catch (e) {
      console.log(e)
    }
  }
  useEffect(() => {
    if (userState !== null) {
      getUserPostgresService(userState.id.toString())
        .then((response) => setColor(response.data.color))
        .catch(() => setColor(undefined))
    }
  }, [userState?.id])
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
