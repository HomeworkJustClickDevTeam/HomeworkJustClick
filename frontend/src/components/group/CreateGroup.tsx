import React, { useContext, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import postgresqlDatabase from "../../services/postgresDatabase"
import { Action, GroupCreate } from "../../types/types"
import DispatchContext from "../../DispatchContext"

function CreateGroup() {
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
    <form onSubmit={handleSubmit}>
      <input
        name="name"
        placeholder="GroupType name"
        onChange={handleChange}
        type="text"
        value={group.name}
      />
      <input
        name="description"
        placeholder="Descritpion of group"
        onChange={handleChange}
        type="text"
        value={group.description}
      />
      <button>Stworz grupe</button>
    </form>
  )
}
export default CreateGroup
