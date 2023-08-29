import React, {useContext, useEffect, useState} from "react"
import {getUserPostgresService, putUserColorPostgresService} from "../../services/postgresDatabaseServices"
import {AxiosError} from "axios"
import {getUser} from "../../services/otherServices";
import {useNavigate} from "react-router-dom";

export default function UserAppearanceSettingsPage() {
  const [color, setColor] = useState<number | undefined>(undefined)
  const userState = getUser()
  const navigate = useNavigate()



  const handleColorChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setColor(+event.target.value)
    try {
      if(userState !== undefined){
        await putUserColorPostgresService(userState.id.toString(), +event.target.value)
          .catch((error: AxiosError) => {
            console.log("AXIOS ERROR: ", error)
          })
      }
    } catch (e) {
      console.log(e)
    }
  }
  useEffect(() => {
    if(userState !== undefined){
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
