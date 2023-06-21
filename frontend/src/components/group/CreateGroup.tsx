import React, { useState } from "react"
import { useNavigate } from "react-router-dom"
import postgresqlDatabase from "../../services/postgresDatabase"
import { GroupCreate } from "../../types/types"

function CreateGroup() {
  const [group, setGroup] = useState<GroupCreate>({
    name: "",
    description: "",
  })
  const navigate = useNavigate()

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      await postgresqlDatabase.post(
        "/group/withTeacher/" + localStorage.getItem("id"),
        group,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
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
      <div className='pl-[3vw] pt-16  '>
          <p className='text-xl border-b-main_blue border-b-solid border-b-2 w-fit mb-8'>Utwórz nową grupę!</p>
            <form onSubmit={handleSubmit}>
              <input
                name="name"
                placeholder="Nazwa grupy"
                onChange={handleChange}
                type="text"
                value={group.name}
                className='mb-12 border-b-2 border-b-light_gray text-center placeholder:text-light_gray placeholder:text-[12px]  mr-4'

              />
              <input
                name="description"
                placeholder="Krótki opis grupy"
                onChange={handleChange}
                type="text"
                value={group.description}
                className='mb-12 border-b-2 border-b-light_gray text-center placeholder:text-light_gray placeholder:text-[12px] '

              />
              <br/>
              <button className='px-3 py border-solid border-main_blue border-2 rounded bg-main_blue text-white text-sm'>Stwórz grupę</button>
            </form>
      </div>
  )
}
export default CreateGroup
