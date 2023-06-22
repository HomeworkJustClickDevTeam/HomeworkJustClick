import React, { useContext, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import postgresqlDatabase from "../../services/postgresDatabase"
import {Action, Group, GroupCreate} from "../../types/types"
import DispatchContext from "../../DispatchContext"
import UserContext from "../../UserContext";


function CreateGroup() {
  const {userState} = useContext(UserContext)
  const [group, setGroup] = useState<GroupCreate>({
    name: "",
    description: "",
  })
  const navigate = useNavigate()
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      await postgresqlDatabase.post("/group/withTeacher/" + userState.userId ,group)
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
      <div className='pl-12 pt-16  '>
          <div className='text-3xl border-b-main_blue border-b-solid border-b-2 w-fit mb-8'>Utwórz nową grupę!</div>
            <form onSubmit={handleSubmit}>
              <input
                name="name"
                placeholder="Nazwa grupy"
                onChange={handleChange}
                type="text"
                value={group.name}
                className='mb-12 border-b-2 border-b-light_gray text-center placeholder:text-light_gray placeholder:text-base  mr-4 w-52'

              />
              <input
                name="description"
                placeholder="Krótki opis grupy"
                onChange={handleChange}
                type="text"
                value={group.description}
                className='mb-12 border-b-2 border-b-light_gray text-center placeholder:text-light_gray placeholder:text-base w-64'

              />
              <br/>
              <button className='px-6 py border-solid border-main_blue border-2 rounded bg-main_blue text-white text-base'>Stwórz grupę</button>
            </form>
      </div>
  )
}
export default CreateGroup
