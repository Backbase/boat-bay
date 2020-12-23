import { Moment } from 'moment';

export interface IUpload {
  id?: number;
  createdOn?: Moment;
  createdBy?: string;
  fileContentType?: string;
  file?: any;
  fileName?: string;
  processed?: boolean;
  action?: string;
  error?: string;
}

export class Upload implements IUpload {
  constructor(
    public id?: number,
    public createdOn?: Moment,
    public createdBy?: string,
    public fileContentType?: string,
    public file?: any,
    public fileName?: string,
    public processed?: boolean,
    public action?: string,
    public error?: string
  ) {
    this.processed = this.processed || false;
  }
}
