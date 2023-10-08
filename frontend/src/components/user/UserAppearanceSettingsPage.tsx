import React, { useState } from "react"
import { changeUserColorPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"

export default function UserAppearanceSettingsPage() {
  const userState = useAppSelector(selectUserState)
  const [color, setColor] = useState<number | undefined>(userState?.color)
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
