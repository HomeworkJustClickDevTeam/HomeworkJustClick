import { setupServer } from "msw/node";
import { homePageHandlers } from "../../mocks/handlers";
import '@testing-library/jest-dom';
import { BrowserRouter } from "react-router-dom";
import HomePage from "../../components/home/HomePage";
import renderer from "react-test-renderer";
import { Provider } from "react-redux";
import { setupStore } from "../../redux/store";
import React from "react";
import { tr } from "date-fns/locale";
import { renderWithProviders } from "../../utils/test-utils";
import userEvent from "@testing-library/user-event";
import { findByText, prettyDOM, queryByText, screen, waitFor, waitForElementToBeRemoved } from "@testing-library/react";
import { use } from "msw/lib/core/utils/internal/requestHandlerUtils";

const server = setupServer(...homePageHandlers)
beforeAll(() => server.listen())
beforeEach(()=> {
  jest.clearAllMocks()
})
afterEach(()=> server.resetHandlers())
afterAll(() => server.close())


it("checks if home page renders correctly with sample groups",async  () => {
  const {asFragment} = renderWithProviders(
    <BrowserRouter>
      <HomePage></HomePage>
    </BrowserRouter>,
    {preloadedState:{userState: {
          token: "SFfnJehuSfRNt3EVYipbT_UNFH09NaGUvGtAK-Mq4SukhrZoJW_F8eVw",
          id: 3,
          role: "USER",
          color: 2,
          name: "Adam",
          lastname: "Nowak",
          index: 222222
        }}})
  await screen.findByText(/Example Group 1/i)
  expect(asFragment()).toMatchSnapshot()
})

it("checks if home page group sorting works",  async () => {
  const user = userEvent.setup()
  const {asFragment} = renderWithProviders(
    <BrowserRouter>
      <HomePage></HomePage>
    </BrowserRouter>,
    {preloadedState:{userState: {
          token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZGFtX25vd2FrQGdtYWlsLmNvbSIsImlhdCI6MTcwMTA2NjIyOCwiZXhwIjoxNzAxMDY5ODI4fQ.gIqB5eSqKchKtFYs17GskZALXXtpVpSFfnJehuSfRNt3EVYipbT_UNFH09NaGUvGtAK-Mq4SukhrZoJW_F8eVw",
          id: 3,
          role: "USER",
          color: 2,
          name: "Adam",
          lastname: "Nowak",
          index: 222222
        }}})
  await screen.findByText(/Example Group 1/i)
  expect(asFragment()).toMatchSnapshot()
  const sortGroupsButton = screen.getByRole('button', {name:/wszystkie grupy/i})
  await user.click(sortGroupsButton)
  const studentGroupsButton = await screen.findByText(/Grupy uczniowskie użytkownika/i)
  expect(asFragment()).toMatchSnapshot()
  await user.click(studentGroupsButton)
  expect(asFragment()).toMatchSnapshot()
})



