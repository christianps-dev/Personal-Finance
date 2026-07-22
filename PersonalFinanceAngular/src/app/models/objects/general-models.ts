export const UserInfoModel: any = {
  username: sessionStorage.getItem('username') || '',
  email: sessionStorage.getItem('email') || '',
};
export const ExpenseCategoriesModel: string[] = [
  'Food',
  'Transport',
  'Leisure',
  'Health',
  'Housing',
  'Others',
];

export const IncomeCategoriesModel: string[] = ['Job', 'Freelance', 'Gift', 'Others'];

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
