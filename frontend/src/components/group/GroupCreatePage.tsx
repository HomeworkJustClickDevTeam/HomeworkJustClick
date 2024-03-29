import React, { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { createGroupWithTeacherPostgresService } from "../../services/postgresDatabaseServices"
import { GroupCreateInterface } from "../../types/GroupCreateInterface"
import { selectUserState } from "../../redux/userStateSlice"
import { setHomePageIn } from "../../redux/homePageInSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import {toast} from "react-toastify";


function GroupCreatePage() {
  const [group, setGroup] = useState<GroupCreateInterface>({
    name: "",
    description: "",
  })
  const dispatch = useAppDispatch()
  const userState = useAppSelector(selectUserState)
  const navigate = useNavigate()
  useEffect(() => {
    dispatch(setHomePageIn(false))
  }, [])
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      await createGroupWithTeacherPostgresService(userState?.id as unknown as string, group)
      navigate("/")
      toast.success("Pomyślnie stworzono grupę")
    } catch (e) {
      console.log(e)
      toast.error("Coś poszło nie tak przy tworzeniu grupy")
    }
  }

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setGroup((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  return (
    <div className='relative flex flex-col pt-16 text-center'>
      <div
        className='mr-auto ml-auto text-4xl border-b-main_blue border-b-solid border-b-2 mb-12 text-center w-fit'>Utwórz
        nową grupę!
      </div>
      <form onSubmit={handleSubmit} className='flex flex-col w-fit mx-auto items-center text-left'>
        <input
          name="name"
          placeholder="Nazwa grupy"
          maxLength={65}
          onChange={handleChange}
          type="text"
          value={group.name}
          className='mb-12 border-b-2 border-b-light_gray placeholder:text-light_gray placeholder:text-base  mr-4 w-52 text-center focus:outline-none focus:border-b-main_blue'

        />
        <input
          name="description"
          placeholder="Krótki opis grupy"
          maxLength={65}
          onChange={handleChange}
          type="text"
          value={group.description}
          className='mb-12 border-b-2 border-b-light_gray placeholder:text-light_gray placeholder:text-base w-72 text-center focus:outline-none focus:border-b-main_blue'

        />
        <br />
        <button
          className='px-6 py-2 border-solid border-main_blue border-2 rounded bg-main_blue text-white text-lg '>Stwórz
          grupę
        </button>
      </form>
    </div>
  )
}

export default GroupCreatePage
