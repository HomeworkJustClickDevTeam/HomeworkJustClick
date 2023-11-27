import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import { createAssignmentWithUserAndGroupPostgresService } from "../../services/postgresDatabaseServices"
import ReactDatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"
import { AssignmentAddFile } from "./AssignmentAddFile"
import { AssignmentToSendInterface } from "../../types/AssignmentToSendInterface"
import { selectGroup } from "../../redux/groupSlice"
import { useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"

function AssignmentAddPage() {
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)

  const [assignment, setAssignment] = useState<AssignmentToSendInterface>({
    title: "",
    completionDatetime: new Date(),
    taskDescription: "",
    visible: false,
    max_points: 1,
    auto_penalty: 50,
  })
  const [toSend, setToSend] = useState<boolean>(false)
  const [idAssignment, setIdAssignment] = useState<number>()
  const [toNavigate, setToNavigate] = useState<boolean>(false)
  const group = useSelector(selectGroup)

  useEffect(() => {
    if (toNavigate) {
      navigate(`/group/${group?.id}/assignments/`)
    }
  }, [toNavigate])

  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: parseInt(value),
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssignment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (userState) {
      createAssignmentWithUserAndGroupPostgresService(
        userState.id.toString(),
        group?.id as unknown as string,
        assignment
      )
        .catch((error) => console.log(error))
        .then((response) => {
          if (response !== undefined) {
            setIdAssignment(response.data.id)
            setToSend(true)
            navigate(-1)
          }
        })
    }
  }

  return (
    <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80">
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        <label className="pr-3">
          Tytuł:
          <input
            name="title"
            type="text"
            onChange={handleTextChange}
            placeholder="Nazwa zadania"
            className="pl-1 ml-2 border-b-2 border-b-light_gray w-64"
          />
        </label>
        <label>
          Opis zadania:
          <input
            name="taskDescription"
            type="text"
            onChange={handleTextChange}
            placeholder="Opis zadania"
            className="pl-1 ml-2 border-b-2 border-b-light_gray w-80"
          />
        </label>
        <label>
          {" "}
          Maksymalne punkty:
          <input
            name="max_points"
            type="number"
            onChange={handleNumberChange}
            min="1"
            max="10"
            className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
          />
        </label>
        <label>
          {" "}
          Kara za wysłanie po terminie (%)
          <input
            name="auto_penalty"
            type="number"
            onChange={handleNumberChange}
            min="0"
            max="100"
            step="25"
            className="pl-1 ml-2 border-b-2 border-b-light_gray cursor-pointer w-12"
          />
        </label>
        <label className="flex">
          <p className="w-36">Data wykonania: </p>
          <ReactDatePicker
            name="completionDatetime"
            selected={assignment.completionDatetime}
            onChange={handleDateChange}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="yyyy-MM-dd HH:mm"
            className="pl-1 ml-2 border-b-2 border-b-light_gray w-36 cursor-pointer"
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
        <button
          type="submit"
          className="absolute top-0 right-0 mr-6 mt-4 px-6 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
        >
          Utwórz zadanie
        </button>
      </form>
      <p className="mt-4 mb-2">Dodaj pliki: </p>
      <AssignmentAddFile
        toSend={toSend}
        idAssignment={idAssignment}
        setToNavigate={setToNavigate}
      />
    </div>
  )
}

export default AssignmentAddPage
