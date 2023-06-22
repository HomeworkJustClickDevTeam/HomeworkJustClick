import { useNavigate, useParams } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import { AssigmentModifyProps } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import ReactDatePicker from "react-datepicker"

import { ModifyAssigmentFile } from "./file/ModifyAssigmentFile"

function ModifyAssigment({ assignment, setAssigment }: AssigmentModifyProps) {
  const navigate = useNavigate()
  const { id } = useParams()
  const [toSend, setToSend] = useState<boolean>(false)
  const [toNavigate, setToNavigate] = useState<boolean>(false)
  useEffect(() => {
    if (toNavigate) {
      navigate(`/group/${id}/assignments/`)
    }
  }, [toNavigate])

  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssigment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssigment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }
  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    postgresqlDatabase
      .put(`/assignment/${assignment.id}`, assignment)
      .then(() => setToSend(true))
      .catch((e) => console.log(e))
  }

  function handleDelete() {
    postgresqlDatabase
      .delete(`/assignment/${assignment.id}`)
      .then(() => navigate(`/group/${id}/assignments/`))
      .catch((e) => console.log(e))
  }
  return (
    <>
      <form onSubmit={handleSubmit}>
        <label>
          Tytuł
          <input
            name="title"
            type="text"
            onChange={handleTextChange}
            value={assignment.title}
          />
        </label>
        <label>
          Punkty
          <input
            name="max_points"
            type="number"
            onChange={handleNumberChange}
            min="1"
            max="10"
            value={assignment.max_points}
          />
        </label>
        <label>
          Opis zadania
          <input
            name="taskDescription"
            type="text"
            onChange={handleTextChange}
            value={assignment.taskDescription}
          />
        </label>
        <ReactDatePicker
          name="completionDatetime"
          selected={assignment.completionDatetime}
          onChange={handleDateChange}
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
            checked={assignment.visible}
            onChange={handleCheckboxChange}
          />
        </label>
        <button type="submit">Zmodyfikuj zadanie domowe</button>
      </form>
      <ModifyAssigmentFile
        toSend={toSend}
        assignmentId={assignment.id}
        setToNavigate={setToNavigate}
      />
      <button onClick={handleDelete}>Usuń Zadanie (USUŃ TOOOOOOOOO)</button>
    </>
  )
}
export default ModifyAssigment
