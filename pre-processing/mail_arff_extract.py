#!/bin/env python3
import os, arff, sys
import email.parser, email.policy

def get_all_payload(mail):
    payload = ""
    if mail.is_multipart():
        for part in mail.get_payload():
            payload += get_all_payload(part)
    else:
        return mail.get_payload()        
    return payload

def parse_mail(filepath):
    with open(filepath, 'rb') as file:
        mail = email.parser.BytesParser().parse(file)
        return [
            str(mail["From"]).replace("\"", "").replace("'", " "),
            str(mail["To"]),
            str(mail["Subject"]),
            mail.get_content_type() == 'text/html',
            get_all_payload(mail).replace("\"", "").replace("'", " ")
        ]

def parse_dir(path, is_spam):
    files = os.listdir(path)
    mails = []
    for file in files: 
        filepath = os.path.join(path, file)
        if os.path.isfile(filepath):
            parsed_mail = parse_mail(filepath)
            parsed_mail.append(is_spam)
            mails.append(parsed_mail)
        if os.path.isdir(filepath):
            submails = parse_dir(filepath, is_spam)
            mails += submails
    return mails


if __name__ == "__main__":

    if len(sys.argv) != 3 :
        print("Usage: {} source_folder output.arff".format(sys.argv[0]))
        sys.exit(1)

    source_folder = sys.argv[1]
    output_file = sys.argv[2]
    cwd = os.path.dirname(sys.argv[0])

    if cwd.strip() != '':
        os.chdir(cwd)

    dirs = os.listdir(source_folder)
    all_mails = []
    for mail_dir in dirs:
        path = os.path.join(source_folder, mail_dir)
        is_spam = mail_dir == "spam"
        mails = parse_dir(os.path.join(source_folder, mail_dir), is_spam)
        print("{} {} mails".format(len(mails), mail_dir))

        all_mails += mails


    arff.dump(
        output_file,
        all_mails,
        relation="mails",
        names=["from", "to", "subject", "html", "body", "spam" ],
        )