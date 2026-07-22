export const UserInfoModel: any = {
  username: sessionStorage.getItem('username') || '',
  email: sessionStorage.getItem('email') || '',
};
export const ExpenseCategoriesModel: string[] = [
  'FOOD',
  'TRANSPORT',
  'LEISURE',
  'HEALTH',
  'HOUSING',
  'OTHERS',
];

export const IncomeCategoriesModel: string[] = ['JOB', 'FREELANCE', 'GIFT', 'Others'];

export const ChartCategories: string[] = [
  'FOOD',
  'TRANSPORT',
  'LEISURE',
  'HEALTH',
  'HOUSING',
  'OTHERS',
  'JOB',
  'FREELANCE',
  'GIFT',
];

export const MonthsModel: string[] = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];
