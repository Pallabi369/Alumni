import React from "react";
import {calculateSumsForYear, PayrollDashboardTile, takeLastYear} from "../PayrollDashboardTile";
import {Employment, PayrollResult} from "../../api";
import {QueryClient, QueryClientProvider} from "react-query";
import {appStore} from "../../store/appStore";
import {fireEvent, render, screen} from "@testing-library/react";
import {BrowserRouter} from "react-router-dom";
import {I18nextProvider, initReactI18next} from "react-i18next";
import i18n from "i18next";
import {cacheableQueryKey} from "../../services/services";

describe('Payroll tile', ()=> {
    describe('Test payroll sum calculation', () => {
        test('calculate payroll sum multi currency', async () => {

            const currency1 = 'Neira';
            const currency2 = 'Cedi';

            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "10000", currency: currency1 } as PayrollResult,
                { sequence: "2", startPayroll: '202002', amountPaid: "10000", currency: currency1 } as PayrollResult,
                { sequence: "3", startPayroll: '201901', amountPaid: "10000", currency: currency1 } as PayrollResult,
                { sequence: "4", startPayroll: '202205', amountPaid: "10000", currency: currency1 } as PayrollResult,
                { sequence: "5", startPayroll: '202001', amountPaid: "10000", currency: currency2 } as PayrollResult,
            )

            const sums = calculateSumsForYear(pr, "2020");

            expect(sums).not.toBeNull();
            expect(sums.length == 2).toBeTruthy();

            for (const sum of sums) {
                if (sum.currency === currency1) {
                    expect(typeof sum.amount === 'number').toBeTruthy()
                    expect(sum.amount === 20000 ).toBeTruthy()
                }
                if (sum.currency === currency2) {
                    expect(typeof sum.amount === 'number').toBeTruthy()
                    expect(sum.amount === 10000 ).toBeTruthy()
                }
            }

        });

        test('calculate payroll sum single currency', async () => {
            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "10000", currency: 'Neira' } as PayrollResult,
            )
            const sums = calculateSumsForYear(pr, "2020");

            expect(sums).not.toBeNull();
            expect(sums.length == 1).toBeTruthy();
            expect(sums[0].amount === 10000)

        });

        test('calculate payroll sum float entries', async () => {
            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "7831.35 ", currency: 'EUR' } as PayrollResult,
                { sequence: "2", startPayroll: '202002', amountPaid: "6491.71 ", currency: 'EUR' } as PayrollResult,
            )
            const sums = calculateSumsForYear(pr, "2020");

            expect(sums).not.toBeNull();
            expect(sums.length == 1).toBeTruthy();
            expect(sums[0].amount === 14323.06).toBeTruthy();

        });

        test('calculate payroll of empty payrolls', async () => {
            const pr: Array<PayrollResult> = [];
            const sums = calculateSumsForYear(pr, "2020");

            expect(sums).not.toBeNull();
            expect(sums.length == 0).toBeTruthy();

        });

        test('calculate payroll of empty year', async () => {
            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "10000", currency: 'Neira' } as PayrollResult,
            )
            const sums = calculateSumsForYear(pr, "");

            expect(sums).not.toBeNull();
            expect(sums.length == 0).toBeTruthy();
        });

    })

    describe('Test last year selection', () => {
        test('last year calculation', async () => {
            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "10000", currency: 'Neira' } as PayrollResult,
            )
            const year = takeLastYear(pr);

            expect(year).not.toBeNull();
            expect(year === '2020').toBeTruthy();
        });

        test('calculate last year of empty payroll', async () => {
            const pr: Array<PayrollResult> = []
            const year = takeLastYear(pr);

            expect(year).toBeUndefined();
        });

    });

    describe('Test component',() => {

        i18n
            .use(initReactI18next)
            .init({
                fallbackLng: 'en',
                resources: { en: { translations: {} } }
            });

        appStore().setCorporateId("");
        appStore().setEmployment({zalarisId: '510-123456789', companyName: 'bmw', companyCode: '123'} as Employment)


        test('Present data', async ()=> {
            const pr = Array.of(
                { sequence: "1", startPayroll: '202001', amountPaid: "10000", currency: 'NOK' } as PayrollResult,
            )

            const queryClient = new QueryClient();
            await queryClient.fetchQuery(cacheableQueryKey('payrollResults'), () => {
                return { data: { payrollResults: pr } }
            });


            render(
                <QueryClientProvider client={queryClient}>
                    <BrowserRouter>
                        <I18nextProvider i18n={i18n}>
                            <PayrollDashboardTile />
                        </I18nextProvider>
                    </BrowserRouter>
                </QueryClientProvider>
            );

            expect(screen.getByText('*****')).toBeTruthy();
            fireEvent.click(await screen.findByTitle('common.clickToShowValue'));
            expect(screen.getByText('10000 NOK')).toBeTruthy();

        })

        test('No payroll data', async ()=> {


            const pr: Array<PayrollResult> = []

            const queryClient = new QueryClient();
            await queryClient.fetchQuery(cacheableQueryKey('payrollResults'), () => {
                return { data: { payrollResults: pr } }
            });


            render(
                <QueryClientProvider client={queryClient}>
                    <BrowserRouter>
                        <I18nextProvider i18n={i18n}>
                            <PayrollDashboardTile />
                        </I18nextProvider>
                    </BrowserRouter>
                </QueryClientProvider>
            );

            expect(screen.getByText('error.payrollResultsEmpty')).toBeTruthy();

        })
    })
})
