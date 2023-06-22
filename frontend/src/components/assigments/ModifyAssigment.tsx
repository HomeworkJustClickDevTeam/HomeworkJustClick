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
    <div className='relative flex flex-col mx-24 mt-6 border border-light_gray border-1 rounded-md pt-4 px-4 h-80'>
      <form onSubmit={handleSubmit} className='flex flex-col gap-3'>
        <label className='pr-3'>
          Tytuł
          <input
            name="title"
            type="text"
            onChange={handleTextChange}
            value={assignment.title}
            placeholder='Nazwa grupy' className='pl-1 ml-2 border-b-2 border-b-light_gray w-64'
          />
        </label>

        <label>
          Opis zadania
          <input
            name="taskDescription"
            type="text"
            onChange={handleTextChange}
            value={assignment.taskDescription}
            placeholder='Opis zadania' className='pl-1 ml-2 border-b-2 border-b-light_gray w-80'
          />
        </label>
        <label>
          Maksymalne punkty
          <input
              name="max_points"
              type="number"
              onChange={handleNumberChange}
              min="1"
              max="10"
              value={assignment.max_points}
              className='pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12'
          />
        </label>
        <label className='flex'>
          <p className='w-36'>Data wykonania: </p>
          <ReactDatePicker
            name="completionDatetime"
            selected={assignment.completionDatetime}
            onChange={handleDateChange}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="yyyy-MM-dd HH:mm"
            className='pl-1 ml-2 border-b-2 border-b-light_gray w-36 cursor-pointer'
          />
        </label>
        <label>
          Widoczne:
          <input
            name="visible"
            type="checkbox"
            checked={assignment.visible}
            onChange={handleCheckboxChange}
          />
        </label>
        <button type="submit" className='absolute top-0 right-0 mr-6 mt-4 px-6 py-1 rounded-lg bg-main_blue text-white'>Edytuj zadanie</button>
      </form>
      <p className='mt-4 mb-2'>Dodaj pliki: </p>
      <ModifyAssigmentFile
        toSend={toSend}
        assignmentId={assignment.id}
        setToNavigate={setToNavigate}
      />

      <button onClick={handleDelete} className='absolute bottom-0 right-0 mr-6 mb-4 px-6 py-1 rounded-lg bg-berry_red text-white'>Usuń Zadanie</button>
    </div>
  )
}
export default ModifyAssigment
