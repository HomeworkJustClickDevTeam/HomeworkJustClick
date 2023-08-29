import React, {useContext, useEffect, useState} from "react"
import {useNavigate} from "react-router-dom"
import {postGroupWithTeacherPostgresService} from "../../services/postgresDatabaseServices"
import DispatchContext from "../../contexts/DispatchContext"
import {ActionType} from "../../types/ActionType";
import {GroupCreateInterface} from "../../types/GroupCreateInterface";
import {getUser} from "../../services/otherServices";


function GroupCreatePage() {
  const userState = getUser()
  const [group, setGroup] = useState<GroupCreateInterface>({
    name: "",
    description: "",
  })
  if(userState === undefined){
    return <>Not logged in</>
  }
  const navigate = useNavigate()
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: ActionType = {
      type: "homePageOut",
    }
    globalDispatch?.dispatch(action)
  }, [])
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      await postGroupWithTeacherPostgresService(userState?.id as unknown as string, group)
      navigate("/")
    } catch (e) {
      console.log(e)
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
      <div className='relative flex flex-col pl-12 pt-16  text-center'>
          <div className='mr-auto ml-auto text-4xl border-b-main_blue border-b-solid border-b-2 mb-12 text-center w-fit'>Utwórz nową grupę!</div>
        <form onSubmit={handleSubmit} className='flex flex-col w-fit mx-auto items-center text-left'>
              <input
                name="name"
                placeholder="Nazwa grupy"
                onChange={handleChange}
                type="text"
                value={group.name}
                className='mb-12 border-b-2 border-b-light_gray placeholder:text-light_gray placeholder:text-base  mr-4 w-52 text-center'

              />
              <input
                name="description"
                placeholder="Krótki opis grupy"
                onChange={handleChange}
                type="text"
                value={group.description}
                className='mb-12 border-b-2 border-b-light_gray placeholder:text-light_gray placeholder:text-base w-72 text-center'

              />
              <br/>
              <button className='px-6 py-2 border-solid border-main_blue border-2 rounded bg-main_blue text-white text-lg'>Stwórz grupę</button>
            </form>
      </div>
  )
}
export default GroupCreatePage
