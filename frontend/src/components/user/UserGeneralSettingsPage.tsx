import React, { useState } from "react"
import { changeUserIndexPostgresService } from "../../services/postgresDatabaseServices"
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
      <form onSubmit={handleIndexSubmit} className='py-2'>
        <label htmlFor="index">Indeks: </label>
        <input
          type="number"
          id="index"
          name="index"
          defaultValue={index}
          onChange={(e) => setIndex(+e.target.value)}
          className='pl-2 w-24'
        />
        <button type="submit" className='absolute bg-main_blue text-white rounded-md text-sm p-1 ml-2 active:mt-0.5 active:shadow-md'>Potwierdź</button>
      </form>
    </>
  )
}
