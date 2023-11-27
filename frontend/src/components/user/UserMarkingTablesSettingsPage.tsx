import React, { useState } from "react"
import PointsTableForm from "./PointsTableForm"
import { useGetEvaluationTable } from "../customHooks/useGetEvaluationTable"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { PointTableDisplayed } from "./PointTableDisplayed"

export default function UserMarkingTablesSettingsPage() {
  const userState = useAppSelector(selectUserState)
  const { evaluationTable, setEvaluationTable } = useGetEvaluationTable(
    userState?.id
  )
  console.log(evaluationTable)
  const [isFormHidden, setIsFormHidden] = useState<boolean>(true)
  return (
    <>
      <PointTableDisplayed tables={evaluationTable} />
      <button
        className="bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none"
        onClick={() => setIsFormHidden(false)}
      >
        Nowa tabela +
      </button>

      {!isFormHidden && <PointsTableForm setIsFormHidden={setIsFormHidden} />}
    </>
  )
}
