import { useNavigate, useParams } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import { Assigment, AssigmentProps } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import ReactDatePicker from "react-datepicker"
import Loading from "../animations/Loading"

function ModifyAssigment({ assignment }: AssigmentProps) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [isLoading, setIsLoading] = useState<boolean>()
  const [modifyAssignment, setModifyAssignment] = useState<Assigment>({
    completionDatetime: new Date(),
    id: 0,
    taskDescription: "",
    title: "",
    visible: false,
    max_points: 0,
    groupId: 0,
  })
  useEffect(() => {
    setModifyAssignment(assignment)
    setIsLoading(false)
  })

  const handleChange = (event: ChangeEvent<HTMLInputElement> | Date) => {
    if (event instanceof Date) {
      setModifyAssignment((prevState) => ({
        ...prevState,
        completionDatetime: event,
      }))
    } else {
      const { name, value, type, checked } = event.target
      setModifyAssignment((prevState) => ({
        ...prevState,
        [name]: type === "checkbox" ? checked : value,
      }))
      console.log(modifyAssignment.visible)
    }
  }

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    try {
      console.log(assignment)
      postgresqlDatabase.put(
        `/assignment/withUserAndGroup/${localStorage.getItem("id")}/${id}`,
        assignment
      )
      navigate(`/group/${id}/assignments/`)
    } catch (e) {
      console.log(e)
    }
  }

  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      <form onSubmit={handleSubmit}>
        <label>
          Tytuł
          <input
            name="title"
            type="text"
            onChange={handleChange}
            value={modifyAssignment.title}
          />
        </label>
        <label>
          Punkty
          <input
            name="points"
            type="number"
            onChange={handleChange}
            min="1"
            value={modifyAssignment.max_points}
          />
        </label>
        <label>
          Opis zadania
          <input
            name="taskDescription"
            type="text"
            onChange={handleChange}
            value={modifyAssignment.taskDescription}
          />
        </label>
        <ReactDatePicker
          name="completionDatetime"
          selected={modifyAssignment.completionDatetime}
          onChange={(date) => handleChange(date as Date)}
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={15}
          dateFormat="yyyy-MM-dd HH:mm"
        />
        <label>
          Visible:
          <input
            name="visible"
            type="checkbox"
            checked={modifyAssignment.visible}
            onChange={handleChange}
          />
        </label>
        <button type="submit">Zmodyfikuj zadanie domowe</button>
      </form>
    </>
  )
}
export default ModifyAssigment
