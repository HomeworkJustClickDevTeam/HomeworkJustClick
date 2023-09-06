import {createContext, Dispatch} from "react";

interface LoadingContextInterface{
  isLoading: boolean
  setIsLoading: Dispatch<boolean>
}

export const LoadingContext = createContext<LoadingContextInterface>(
  {isLoading: true, setIsLoading: () => {}}
)